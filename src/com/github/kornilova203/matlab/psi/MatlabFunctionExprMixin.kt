package com.github.kornilova203.matlab.psi

import com.github.kornilova203.matlab.psi.types.MatlabType
import com.github.kornilova203.matlab.psi.types.MatlabTypeClass
import com.github.kornilova203.matlab.psi.types.MatlabTypeFunction
import com.github.kornilova203.matlab.psi.types.MatlabTypeUnknown
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

abstract class MatlabFunctionExprMixin(node: ASTNode) : MatlabASTWrapperPsiElement(node), MatlabFunctionExpr, MatlabTypedExpr {
    override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {
        if (this != place.parent) {
            return true
        }
        val type = firstArgument()?.getType()
        if (type !is MatlabTypeClass) {
            return true
        }
        val classDeclaration = type.decl
        val methods = classDeclaration.methodsBlockList
        for (method in methods) {
            val functionList = method.methodsList?.functionDeclarationList ?: return true
            for (function in functionList) {
                processor.execute(function, state)
            }
        }
        return (processor as? MatlabResolvingScopeProcessor)?.declaration == null
    }

    override fun getType(): MatlabType {
        val expr = this.expr
        if (expr !is MatlabTypedExpr) {
            return MatlabTypeUnknown()
        }
        val type = expr.getType()
        if (type is MatlabTypeFunction) {
            return type.getReturnType()
        }
        if (type is MatlabClassDeclaration) {
            if (this.parent !is MatlabQualifiedExpr) {
                if (firstArgument()?.getType()?.getName() == type.getName()) {
                    return type
                }
                return MatlabTypeUnknown()
            }
        }
        return type
    }

    private fun firstArgument(): MatlabTypedExpr? {
        val children = arguments.children
        return if (children.isNotEmpty()) children[0] as? MatlabTypedExpr else null
    }
}