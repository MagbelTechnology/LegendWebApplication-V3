/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.handlers;

import com.magbel.admin.dao.MagmaDBConnection;

/**
 *
 * @author Developer - Ganiyu
 */
public class Supervisor extends MagmaDBConnection {
 ApprovalRecords aprecords = null;
    public Supervisor() {
            aprecords = new ApprovalRecords();
	}
}
