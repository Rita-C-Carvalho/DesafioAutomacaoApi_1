package br.com.teste.accenture.tools;

import br.com.teste.accenture.exceptions.ExceptionMessages;
import br.com.teste.accenture.exceptions.TesteAccentureExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

import static br.com.teste.accenture.utils.Constants.*;

public class PropertiesLoader {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    public static String getApiPropertie(String chave, String sistema) {
        String fileName = PROPERTIES_CAMINHO + sistema + PROPERTIES_EXTENSAO;
        Properties props = new Properties();

        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new TesteAccentureExceptions(ExceptionMessages.ARQUIVO_PROPERTIES_NAO_ENCONTRADO + fileName);
            }
            props.load(input);

        } catch (IOException e) {
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_LER_ARQUIVO_PROPERTIES + fileName, e);
        } catch (Exception e) {
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_CARREGAR_ARQUIVO_PROPERTIES + fileName, e);
        }

        String valor = props.getProperty(chave);
        if (valor == null) {
            throw new TesteAccentureExceptions(ExceptionMessages.CHAVE_NAO_ENCONTRADA_NO_ARQUIVO_PROPERTIES + chave + fileName);
        }

        logger.info("Carregando propriedade '" + chave + "' do arquivo " + fileName + ": " + valor);
        return valor;
    }

    // Salva no System Properties
    public static void setSystemProperty(String chave, String valor, String sistema) {
        String systemKey = sistema + "." + chave;
        System.setProperty(systemKey, valor);
        logger.info("Propriedade '" + chave + "' salva no System Properties: " + valor);
    }

    // Salva múltiplas propriedades no System Properties
    public static void setMultipleSystemProperties(Properties newProperties, String sistema) {
        for (String key : newProperties.stringPropertyNames()) {
            String value = newProperties.getProperty(key);
            System.setProperty(sistema + "." + key, value);
            logger.debug("System Property salva: " + sistema + "." + key + " = " + value);
        }
        logger.info("Múltiplas propriedades salvas no System Properties para sistema: " + sistema);
    }

    // Para buscar do System Properties
    public static String getFromSystem(String chave, String sistema) {
        String systemKey = sistema + "." + chave;
        String valor = System.getProperty(systemKey);

        if (valor != null) {
            logger.info("Propriedade '" + chave + "' obtida do System Properties: " + valor);
            return valor;
        } else {
            logger.warn("Propriedade '" + chave + "' não encontrada no System Properties para sistema: " + sistema);
            return null;
        }
    }


    //Para verificar se existe no System
    public static boolean propertyExistsInSystem(String chave, String sistema) {
        String systemKey = sistema + "." + chave;
        return System.getProperty(systemKey) != null;
    }


    // Para mostrar todas as propriedades do System Properties
    public static void printAllSystemProperties(String sistema) {
        String prefix = sistema + ".";
        logger.info("=== System Properties para sistema: " + sistema + " ===");

        for (String key : System.getProperties().stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                String value = System.getProperty(key);
                logger.info(key + " = " + value);
            }
        }

        logger.info("=== Fim das System Properties ===");
    }

}