package com.jumedev.bookstore.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);
}
