package com.acme.backendfreshsense.shared.infrastructure.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/**
 * Punto único para obtener el id del usuario autenticado.
 *
 * <p>{@link JwtAuthFilter} deja el {@code userId} en el {@code SecurityContext}
 * vía {@code auth.setDetails(userId)}. Esta utilidad lo lee de ahí, evitando
 * duplicar la extracción del token en cada controller (antes: helper
 * {@code extractUserId} copiado en 5 controllers).</p>
 */
public final class CurrentUser {

    private CurrentUser() {}

    /** @return id del usuario autenticado; 401 si no hay sesión válida. */
    public static Long id() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getDetails() instanceof Long userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
        }
        return userId;
    }
}
