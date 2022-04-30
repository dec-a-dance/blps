package com.example.blps.security;

import com.example.blps.entities.AuthToken;
import com.example.blps.repositories.AuthTokenRepository;
import com.example.blps.repositories.UserRepository;
import com.example.blps.util.XmlReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final XmlReader xml;

    @Override
    public void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
        throws ServletException, IOException {
        Optional<String> optionalJwt = getTokenFromRequest(request);
        if (optionalJwt.isPresent() && jwtUtil.tokenIsValid(optionalJwt.get())){
            String username = jwtUtil.subjectFromToken(optionalJwt.get());
            AuthToken authToken = xml.getToken(username);
            if (authToken == null){
                throw new RuntimeException("");
            }
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(authToken, null, authToken.getAuthorities())
            );
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if (bearer == null) return Optional.empty();
        return bearer.startsWith("Bearer ") ? Optional.of(bearer.substring(7)) : Optional.empty();
    }
}
