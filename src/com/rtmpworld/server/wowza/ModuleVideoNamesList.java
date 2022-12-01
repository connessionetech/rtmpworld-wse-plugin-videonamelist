package com.rtmpworld.server.wowza;

import com.wowza.wms.application.*;
import com.wowza.wms.amf.*;
import com.wowza.wms.client.*;
import com.wowza.wms.module.*;
import com.wowza.wms.request.*;
import com.wowza.wms.stream.*;
import com.wowza.wms.rtp.model.*;
import com.wowza.wms.httpstreamer.model.*;
import com.wowza.wms.httpstreamer.cupertinostreaming.httpstreamer.*;
import com.wowza.wms.httpstreamer.smoothstreaming.httpstreamer.*;
import com.wowza.wms.logging.WMSLogger;

public class ModuleVideoNamesList extends ModuleBase {
	
	public static String MODULE_NAME = "ModuleVideoNamesList";
	
	private WMSLogger logger;
	
	protected static boolean moduleDebug;


	public void onAppCreate(IApplicationInstance appInstance)
	{
		this.logger = getLogger();
		
		if(moduleDebug){
			this.logger.info(MODULE_NAME + ".onAppCreate");
		}
		
		this.readProperties();
	}
	
	
	
	private void readProperties()
	{
		if(moduleDebug){
			this.logger.info(MODULE_NAME + ".readProperties => reading properties");
		}
		
	}

}
