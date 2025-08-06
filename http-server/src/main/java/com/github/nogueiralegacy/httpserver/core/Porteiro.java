package com.github.nogueiralegacy.httpserver.core;

import com.github.nogueiralegacy.httpserver.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class Porteiro {
    private static final Logger logger = LoggerFactory.getLogger(Porteiro.class);
    private static int quantidadeClientesRecebidos = 0;
    private final static int PORT = Configuration.getPort();
    private final static ServerSocket SERVER_SOCKET;

    static {
        // Abre a porta do servidor
        try {
            SERVER_SOCKET = new ServerSocket(PORT);
            logger.info("Servidor HTTP iniciado na porta " + PORT);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o socket do servidor", e);
        }
    }

    private static Cliente recebeCliente() {
        try {
            Socket socket = SERVER_SOCKET.accept();
            quantidadeClientesRecebidos++;
            logger.info("Quantidade de clientes: " + quantidadeClientesRecebidos);

            return new Cliente(socket);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao receber o cliente", e);
        }
    }

    public static void despedeCliente(Cliente cliente) {
        try {
            cliente.getSOCKET().close();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fechar o socket do cliente", e);
        }
    }

    public static void comecaTurno() {
        while (true) {
            Cliente cliente = Porteiro.recebeCliente();

            // Chama recepcionista
            Recepcionista.repassaCliente(cliente);
        }
    }
}