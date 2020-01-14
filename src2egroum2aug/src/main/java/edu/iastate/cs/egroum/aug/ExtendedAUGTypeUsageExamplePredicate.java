package edu.iastate.cs.egroum.aug;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * HJ: same as TypeUsageExamplePredicate.java
 * but! we check the method name too, instead of only checking the type
 * @author kanghongjin
 *
 */
public class ExtendedAUGTypeUsageExamplePredicate implements UsageExamplePredicate {
    private final Set<String> fullyQualifiedTypeNames;
    private final Set<String> simpleTypeNames;
    
    private final String methodName;
    private final boolean isCtor;

    public static ExtendedAUGTypeUsageExamplePredicate EAUGUsageExamplesOf(String methodName, String... fullyQualifiedTypeNames) {
    	if (fullyQualifiedTypeNames.length == 0) throw new RuntimeException("wrong");
        return new ExtendedAUGTypeUsageExamplePredicate(methodName, fullyQualifiedTypeNames);
    }

    protected ExtendedAUGTypeUsageExamplePredicate(String methodName, String... fullyQualifiedTypeNames) {
        this.fullyQualifiedTypeNames = new HashSet<>(Arrays.asList(fullyQualifiedTypeNames));
        this.simpleTypeNames = new HashSet<>();
        for (String fullyQualifiedTypeName : fullyQualifiedTypeNames) {
            simpleTypeNames.add(fullyQualifiedTypeName.substring(fullyQualifiedTypeName.lastIndexOf('.') + 1));
        }
        
        this.methodName = methodName;
        this.isCtor = methodName.contains("init>");
    }

    @Override
    public boolean matches(String sourceFilePath, CompilationUnit cu) {
        return matches(cu);
    }

    @Override
    public boolean matches(MethodDeclaration methodDeclaration) {
        return matches((ASTNode) methodDeclaration);
    }

    private boolean containing;
    private boolean matches(ASTNode node) {
        if (matchesAnyExample()) return true;

        containing = false;
        node.accept(new ASTVisitor(false) {
            @Override
            public boolean visit(MethodInvocation node) {
                return !isDeclaredByApiClass(node.resolveMethodBinding(), false) && super.visit(node);
            }

            @Override
            public boolean visit(ConstructorInvocation node) {
                return !isDeclaredByApiClass(node.resolveConstructorBinding(), true) && super.visit(node);
            }

            @Override
            public boolean visit(ClassInstanceCreation node) {
                return !isDeclaredByApiClass(node.resolveConstructorBinding(), true) && super.visit(node);
            }

            private boolean isDeclaredByApiClass(IMethodBinding mb, boolean isCtor2) {
            	if (isCtor2 && isCtor) {
            		containing = true;
            		return true;
            	}
            	
                if (mb != null) {
                	if (!mb.getName().equals(methodName)) {
                		return false;
                	}
                	
                	Set<String> names = new HashSet<>(); 
                	
                	Stack<ITypeBinding> traversing = new Stack<>();
                	traversing.add(mb.getDeclaringClass().getTypeDeclaration());
                	
                	while (!traversing.isEmpty()) {
                		ITypeBinding current = traversing.pop();
                		
                		if (current.getSuperclass() != null) {
                			traversing.add(current.getSuperclass());
                		}
	                	for (ITypeBinding interfaceObj : current.getInterfaces()) {
	                		
	                		traversing.add(interfaceObj);
	                	}
	                	
	                	if (!current.getQualifiedName().contains("<")) {
	                		names.add(current.getQualifiedName());
	                	} else {
	                		// we ignore things in the diamond, e.g. <K, T> etc.. 
	                		names.add(current.getQualifiedName().split("<")[0]);
	                	}
                	}
                    
                    names.retainAll(fullyQualifiedTypeNames);
                    if (!names.isEmpty()) {
                        containing = true;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean preVisit2(ASTNode node) {
                return !containing && super.preVisit2(node);
            }
        });
        return containing;
    }

    @Override
	public boolean matches(EGroumGraph graph) {
        return matchesAnyExample() || !Collections.disjoint(graph.getAPIs(), simpleTypeNames);
    }

    private boolean matchesAnyExample() {
        return fullyQualifiedTypeNames.isEmpty();
    }
}
