package magma.net.manager;

import magma.net.dao.MagmaDBConnection;

public class Export2Manager extends MagmaDBConnection {
    public Export2Manager() {
    }


    public void fuelRaiseEntry(String vendorAcct, String operateType,
                               String assetId, String categoryId) {

    }

    public void licenceRaiseEntry(String vendorAcct, String operateType,
                                  String assetId, String categoryId) {

    }

    public void accidentRaiseEntry(String vendorAcct, String operateType,
                                   String assetId, String categoryId) {

    }

    public static void main(String[] args) {
        System.out.println("Hello!!!");
        Export2Manager exp = new Export2Manager();

    }


}