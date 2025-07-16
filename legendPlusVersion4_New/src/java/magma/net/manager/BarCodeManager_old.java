package magma.net.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.DriverManager;

import magma.net.vao.BarCodeDetail;
import magma.net.dao.MagmaDBConnection;
import java.util.ArrayList;


public class BarCodeManager_old extends MagmaDBConnection {


    public BarCodeManager_old() {
        System.out.println("INFO: Using BarCodeManager...");

    }


    public BarCodeDetail findBarcodeByID(String assetid) {

        BarCodeDetail barCode = null; 
        String filter = "WHERE ASSET_ID = '" + assetid + "'";
        ArrayList barcodes = findBarcodeByQuery(filter);
        if (barcodes.size() > 0) {
            barCode = (BarCodeDetail) barcodes.get(0);
        }

        return barCode;

    }

    /**
     * findBarcodeQuery
     *
     * @param queryFilter String
     * @return ArrayList
     */
    public ArrayList findBarcodeByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,ASSET_DESC  " +
                "FROM AM_BARCODE_IMAGE  " + queryFilter;

        try {
            con = getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String assetId = rs.getString("ASSET_ID");
                String assetReg = rs.getString("REGISTRATION_NO");
                String assetDesc = rs.getString("ASSET_DESC");

                BarCodeDetail barcode = new BarCodeDetail(assetId, assetReg,
                        assetDesc);
                list.add(barcode);

            }

        } catch (Exception e) {
            System.out.println(
                    "INFO:Error Fecthing Barcode Record Details ->" +
                    e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }


}
