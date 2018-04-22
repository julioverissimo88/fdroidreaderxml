package models;

public class App {
    private String AppLicense;
    private String AppId;
    private String repository;

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getAppLicense() {
        return AppLicense;
    }

    public void setAppLicense(String appLicense) {
        AppLicense = appLicense;
    }
}
