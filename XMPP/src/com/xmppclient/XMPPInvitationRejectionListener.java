package com.xmppclient;

import org.jivesoftware.smackx.muc.InvitationRejectionListener;

import android.util.Log;

public class XMPPInvitationRejectionListener implements InvitationRejectionListener {

	@Override
	public void invitationDeclined(String invitee, String reason) {
		// TODO Auto-generated method stub
		Log.d("_XMPP_", "invitation rejected " + invitee + reason);
	}

}
