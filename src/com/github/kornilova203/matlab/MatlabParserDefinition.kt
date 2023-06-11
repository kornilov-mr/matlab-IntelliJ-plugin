package com.github.kornilova203.matlab

import com.github.kornilova203.matlab.lexer.MatlabLexer
import com.github.kornilova203.matlab.psi.MatlabFile
import com.github.kornilova203.matlab.psi.MatlabTypes
import com.github.kornilova203.matlab.stub.MatlabStubFileElementType
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.IStubFileElementType
import com.intellij.psi.tree.TokenSet

class MatlabParserDefinition : ParserDefinition {
    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create(MatlabTypes.COMMENT)
        val FILE = MatlabStubFileElementType()
    }

    override fun createParser(project: Project?): PsiParser = MatlabParser()

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return MatlabFile(viewProvider)
    }

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun createLexer(p0: Project?): Lexer = MatlabLexer.getAdapter()

    override fun createElement(node: ASTNode?): PsiElement {
        return MatlabTypes.Factory.createElement(node);
    }

    override fun getCommentTokens(): TokenSet = COMMENTS
}