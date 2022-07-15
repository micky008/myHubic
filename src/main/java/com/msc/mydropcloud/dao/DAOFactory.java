package com.msc.mydropcloud.dao;

/**
 *
 * @author Michael
 */
public class DAOFactory {

    public static final SaveSystemDAO ssdao = new SaveSystemDAOJsonImpl();
    public static final GetSystemDAO getdao = new GetSystemDAOImpl();
    public static final ConfigDAO configdao = new ConfigDAOImpl();
    
}
