package br.com.opensig.comercial.server.rest;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BooleanToInteger extends XmlAdapter<Boolean, Integer> {

	@Override
	public Boolean marshal(Integer arg0) throws Exception {
		return arg0 > 0;
	}

	@Override
	public Integer unmarshal(Boolean arg0) throws Exception {
		return arg0 ? 1 : 0;
	}

}
