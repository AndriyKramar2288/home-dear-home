package org.banew.hdh.core.api.users.forms;

public record RegisterForm (
        String username,
        String password,
        String confirmPassword,
        String email
) { }