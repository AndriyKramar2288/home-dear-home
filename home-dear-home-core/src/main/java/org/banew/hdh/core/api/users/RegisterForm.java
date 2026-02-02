package org.banew.hdh.core.api.users;

public record RegisterForm(
        String username,
        String password,
        String confirmPassword,
        String email
) { }