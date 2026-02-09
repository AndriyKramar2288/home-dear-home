package org.banew.hdh.fxapp.implementations;

import org.banew.hdh.fxapp.implementations.xml.XmlUserInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class AuthorizationContext {
    private final AtomicReference<XmlUserInfo> currentUser = new AtomicReference<>();

    public void logout() {
        currentUser.set(null);
    }

    public XmlUserInfo getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(XmlUserInfo userInfo) {
        currentUser.set(userInfo);
    }
}
