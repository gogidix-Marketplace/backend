package com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model;

public final class CargoNexusUserTypes {

    private CargoNexusUserTypes() {}

    public static final String PETROLEUM_MARKETER = "PETROLEUM_MARKETER";
    public static final String DEPOT_OPERATOR = "DEPOT_OPERATOR";
    public static final String TRANSPORT_PARTNER = "TRANSPORT_PARTNER";
    public static final String DRIVER = "DRIVER";
    public static final String INSURER = "INSURER";
    public static final String BANK_OFFICER = "BANK_OFFICER";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String COMMAND_CENTER_OPERATOR = "COMMAND_CENTER_OPERATOR";
    public static final String REGULATORY_AUDITOR = "REGULATORY_AUDITOR";

    public static final String METADATA_KEY_USER_TYPE = "cargonexus_user_type";
    public static final String METADATA_KEY_COMPANY_NAME = "company_name";
    public static final String METADATA_KEY_CAC_NUMBER = "cac_registration_number";
    public static final String METADATA_KEY_DPR_LICENSE = "dpr_license_number";
    public static final String METADATA_KEY_BVN = "bvn";
    public static final String METADATA_KEY_FLEET_SIZE = "fleet_size";
    public static final String METADATA_KEY_DEPOT_ID = "depot_id";
    public static final String METADATA_KEY_INSURER_LICENSE = "insurer_license_number";
    public static final String METADATA_KEY_BANK_ID = "bank_identifier";

    public static final String[] PETROLEUM_USER_TYPES = {
        PETROLEUM_MARKETER, DEPOT_OPERATOR, TRANSPORT_PARTNER,
        DRIVER, INSURER, BANK_OFFICER, CUSTOMER,
        COMMAND_CENTER_OPERATOR, REGULATORY_AUDITOR
    };

    public static boolean isPetroleumUser(String userType) {
        if (userType == null) return false;
        for (String type : PETROLEUM_USER_TYPES) {
            if (type.equals(userType)) return true;
        }
        return false;
    }

    public static boolean requiresDPRLicense(String userType) {
        return PETROLEUM_MARKETER.equals(userType) || DEPOT_OPERATOR.equals(userType);
    }

    public static boolean requiresCAC(String userType) {
        return PETROLEUM_MARKETER.equals(userType) || TRANSPORT_PARTNER.equals(userType)
            || DEPOT_OPERATOR.equals(userType) || INSURER.equals(userType) || BANK_OFFICER.equals(userType);
    }
}
