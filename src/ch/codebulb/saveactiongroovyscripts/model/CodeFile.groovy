package ch.codebulb.saveactiongroovyscripts.model

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import groovy.transform.CompileStatic

/**
 * A wrapper for {@link VirtualFile} providing a simplified file access API.
 */
@CompileStatic
class CodeFile {
    @Delegate
    private final VirtualFile virtualFile
    private final Document document = FileDocumentManager.instance.getDocument(virtualFile)

    public CodeFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile
    }

    /**
     * Gets the file content as text
     * @return the file content
     */
    public String getText() {
        return document.text
    }

    public void setText(String text) {
        document.text = text
    }

    /**
     * Gets the file's parent folder.
     * @return the parent folder
     */
    public CodeFile getParent() {
        return new CodeFile(virtualFile.parent)
    }

    /**
     * Gets the file's child files. Returns an empty {@link CodeFile[]} array if the file itself is not a folder
     * @return the child files
     */
    public CodeFile[] getChildren() {
        return virtualFile.children.collect {VirtualFile it -> new CodeFile(it)} as CodeFile[]
    }

    /**
     * Implements the functionality to navigate through a file hierarchy tree with property access, e.g. with
     * <code>this.subfolder.subsubfolder."file.java"</code>.
     * @param the child file name
     * @return the child file
     */
    public CodeFile propertyMissing(String name) {
        return findOrCreateChild(name)
    }

    /**
     * Implements the functionality to navigate through a file hierarchy tree with property access,
     * and invoking {@link #setText(String)} and {@link #save()} on the child file, e.g. with
     * <code>this.subfolder.subsubfolder."file.java"</code>.
     * @param the child file name
     * @return the child file
     */
    public CodeFile propertyMissing(String name, String value) {
        CodeFile file = findOrCreateChild(name)
        file.text = value
        file.save()
        return file
    }

    /**
     * Gets the child file, if it exists, or creates an empty file and returns the newly created file otherwise.
     * @param name the child file's name
     * @return the child file
     */
    public CodeFile findOrCreateChild(String name) {
        VirtualFile found = virtualFile.findChild(name)
        if (found != null) {
            return new CodeFile(found)
        }

        if (name.contains('.')) {
            return new CodeFile(virtualFile.createChildData(virtualFile, name))
        }
        else {
            return new CodeFile(virtualFile.createChildDirectory(virtualFile, name))
        }
        return new CodeFile(virtualFile.findOrCreateChildData(virtualFile, name))
    }

    /**
     * Synchronizes changes with {@link #setText(String)} to the file system. If the file is opened in an IDE editor
     * window, the window content is automatically updated.
     */
    public void save() {
        FileDocumentManager.instance.saveDocument(document)
    }
}
