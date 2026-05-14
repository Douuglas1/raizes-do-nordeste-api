package com.raizesnordeste.api.infrastructure;

import com.raizesnordeste.api.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);
}