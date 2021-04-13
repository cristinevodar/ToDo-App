package com.ToDo.app.security;

import com.ToDo.backend.data.entity.User;

@FunctionalInterface
public interface CurrentUser {

	User getUser();
}
