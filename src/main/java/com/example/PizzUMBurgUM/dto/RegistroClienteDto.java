package com.example.PizzUMBurgUM.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class RegistroClienteDto {

    /* ===== Datos de cuenta ===== */

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    @NotBlank(message = "Debes confirmar la contraseña")
    private String confirmarContrasena;

    /* ===== Dirección ===== */

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    /* ===== Fecha de nacimiento ===== */

    @NotNull(message = "Selecciona el día de nacimiento")
    private Integer diaNacimiento;

    @NotNull(message = "Selecciona el mes de nacimiento")
    private Integer mesNacimiento;

    @NotNull(message = "Selecciona el año de nacimiento")
    private Integer anioNacimiento;

    /* ===== Teléfono ===== */

    @NotBlank(message = "Selecciona el código de país")
    @Pattern(regexp = "\\+\\d{1,3}", message = "Código de país inválido")
    private String codigoPais;

    // El JS ya envía solo dígitos; aquí validamos longitud
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{8,9}", message = "El teléfono debe tener 8 o 9 dígitos")
    private String tel;

    /* ===== Tarjeta (desde el modal) ===== */

    @NotBlank(message = "Debes seleccionar un método de pago")
    private String metodoPago;  // Visa / MasterCard / American Express

    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Pattern(regexp = "\\d{13,19}", message = "El número de tarjeta debe tener entre 13 y 19 dígitos")
    private String numeroTarjeta;   // sin espacios (JS lo limpia)

    @NotBlank(message = "El nombre en la tarjeta es obligatorio")
    private String nombreTarjeta;

    @NotBlank(message = "La fecha de vencimiento es obligatoria")
    // Formato MM/YYYY
    @Pattern(
            regexp = "(0[1-9]|1[0-2])/\\d{4}",
            message = "El vencimiento debe tener formato MM/YYYY"
    )
    private String vencimientoTarjeta;

    @NotBlank(message = "El CVV es obligatorio")
    @Pattern(regexp = "\\d{3,4}", message = "El CVV debe tener 3 o 4 dígitos")
    private String cvvTarjeta;
}
