package mx.unam.primera.com.model;

/**
 * Esta clase pretende optimizar el acceso a los datos compartidos de la aplicación
 * en temas de configuración de manera más simple.
 */
public class ConfigData {
    private static String domainUrl = "http://18.221.160.127/";
    private static String apiUrl = "guide-ws/service/scripts/rest/";

    public static String getDomainUrl()
    {
        return domainUrl;
    }

    public static String getApiUrl()
    {
        return apiUrl;
    }

    public static String getFullApiUrl()
    {
        return domainUrl+apiUrl;
    }
}
