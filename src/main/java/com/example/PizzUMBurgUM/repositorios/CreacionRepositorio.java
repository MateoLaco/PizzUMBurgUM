package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Creacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreacionRepositorio extends JpaRepository<Creacion, Integer> {
    List<Creacion> findByCreador(Cliente creador);
    List<Creacion> findByCreadorAndTipo(Cliente creador, char tipo);
    List<Creacion> findByCreadorAndFavorito(Cliente creador, boolean favorito);
    @Query("SELECT c FROM Creacion c WHERE c.creador = :creador AND c.tipo= :tipo ORDER BY c.idCreacion DESC LIMIT 3")
    List<Creacion> findTop3ByClienteAndTipoOrderById(@Param("creador")Cliente creador, @Param("tipo") char tipo);
}
