package com.stereowalker.obville.config;

import com.stereowalker.unionlib.config.ConfigObject;
import com.stereowalker.unionlib.config.UnionConfig;
import com.stereowalker.unionlib.util.ScreenHelper.ScreenOffset;

import net.minecraftforge.fml.config.ModConfig.Type;

@UnionConfig(folder = "Obville Configs", name = "client", translatableName = "config.obville.client.file", autoReload = true, appendWithType = false)
public class ClientConfig implements ConfigObject {	
	
	@UnionConfig.Entry(name = "Welcome Color", type = Type.CLIENT)
	public int welcome = 0x4FD65F;
	
	@UnionConfig.Entry(name = "Neutral Color", type = Type.CLIENT)
	public int neutral = 0xFFFFFF;
	
	@UnionConfig.Entry(name = "Weary Color", type = Type.CLIENT)
	public int weary = 0x703E24;
	
	@UnionConfig.Entry(name = "Distrusted Color", type = Type.CLIENT)
	public int distrusted = 0xBE0606;
	
	@UnionConfig.Entry(name = "Exiled Color", type = Type.CLIENT)
	public int exiled = 0x555555;
	
	@UnionConfig.Entry(name = "Reputation Text Position", type = Type.CLIENT)
	public ScreenOffset reputationPosition = ScreenOffset.TOP;
	
}
