package com.example.PizzUMBurgUM.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RegistroClienteDto {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "Mínimo 6 caracteres")
    private String contrasena;

    @NotBlank(message = "Debes confirmar la contraseña")
    private String confirmarContrasena;

    @NotBlank(message = "Selecciona un método de pago")
    private String metodoPago = "";

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    // Teléfono LOCAL (sin código país): 7 a 9 dígitos (ajústalo a tu regla)
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{7,9}", message = "Teléfono local: 7 a 9 dígitos")
    private String tel;

    @NotBlank(message = "Selecciona un código de país")
    // +598, +54, +1, etc.
    @Pattern(regexp = "\\+\\d{1,3}", message = "Código país inválido")
    private String codigoPais;

    @NotNull(message = "Selecciona el día")
    private Integer diaNacimiento;

    @NotNull(message = "Selecciona el mes")
    private Integer mesNacimiento;

    @NotNull(message = "Selecciona el año")
    private Integer anioNacimiento;

    // Solo dígitos, 13 a 19 (regla usual). Si usas espacios, limpiaremos en el controlador.
    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Pattern(regexp = "[\\d ]{13,23}", message = "Tarjeta: 13 a 19 dígitos (puede tener espacios)")
    private String numeroTarjeta;
}
