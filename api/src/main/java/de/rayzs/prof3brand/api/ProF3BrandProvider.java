package de.rayzs.prof3brand.api;

public class ProF3BrandProvider {


    private static ProF3Brand INSTANCE;


    public static ProF3Brand get() {
        if (INSTANCE == null) {
            throw new RuntimeException("API is not initialized yet!");
        }

        return INSTANCE;
    }

    public static void set(ProF3Brand instance) {
        if (ProF3BrandProvider.INSTANCE != null) {
            throw new RuntimeException("API is already set!");
        }

        ProF3BrandProvider.INSTANCE = instance;
    }

}
