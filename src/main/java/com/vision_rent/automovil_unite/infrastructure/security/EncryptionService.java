package com.vision_rent.automovil_unite.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Servicio para encriptar y desencriptar información sensible.
 * Utiliza AES-GCM, un cifrado de flujo autenticado recomendado para datos sensibles.
 */
@Service
public class EncryptionService {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    
    @Value("${app.security.encryption-key}")
    private String encryptionKey;
    
    /**
     * Encripta una cadena utilizando AES-GCM.
     * 
     * @param plainText Texto a encriptar
     * @return Texto encriptado en formato Base64
     */
    public String encrypt(String plainText) {
        try {
            if (plainText == null) {
                return null;
            }
            
            // Generar un IV aleatorio
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            
            // Preparar la clave secreta
            SecretKey secretKey = new SecretKeySpec(
                    encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
            
            // Inicializar el cifrador
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            
            // Encriptar el texto
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            
            // Combinar IV y texto cifrado
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            
            // Codificar en Base64 para almacenamiento/transmisión
            return Base64.getEncoder().encodeToString(byteBuffer.array());
            
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }
    
    /**
     * Desencripta una cadena utilizando AES-GCM.
     * 
     * @param encryptedText Texto encriptado en formato Base64
     * @return Texto desencriptado
     */
    public String decrypt(String encryptedText) {
        try {
            if (encryptedText == null) {
                return null;
            }
            
            // Decodificar de Base64
            byte[] cipherMessage = Base64.getDecoder().decode(encryptedText);
            
            // Extraer IV
            ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);
            
            // Extraer texto cifrado
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);
            
            // Preparar la clave secreta
            SecretKey secretKey = new SecretKeySpec(
                    encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
            
            // Inicializar el descifrador
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            // Desencriptar
            byte[] plainText = cipher.doFinal(cipherText);
            
            return new String(plainText, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }
    
    /**
     * Obtiene una versión enmascarada del número de tarjeta mostrando solo los últimos 4 dígitos.
     * 
     * @param cardNumber Número de tarjeta completo
     * @return Número de tarjeta enmascarado (p.ej. **** **** **** 1234)
     */
    public String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        
        // Mostrar solo los últimos 4 dígitos
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        StringBuilder masked = new StringBuilder();
        
        // Formato: **** **** **** XXXX
        for (int i = 0; i < 3; i++) {
            masked.append("**** ");
        }
        masked.append(lastFourDigits);
        
        return masked.toString();
    }
}