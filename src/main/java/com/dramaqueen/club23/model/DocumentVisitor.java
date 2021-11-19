package com.dramaqueen.club23.model;

public interface DocumentVisitor {
    void visitProperty(String name, String value);
}
