/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */
package org.restcomm.sbc.dao;

import java.util.List;

import org.restcomm.sbc.bo.BanList;
import org.restcomm.sbc.bo.BanListFilter;



/**
 * @author  ocarriles@eolos.la (Oscar Andres Carriles)
 * @date    22 jul. 2016 17:04:39
 * @class   BanListDao.java
 *
 */
public interface BanListDao {
    
	void addBanList(BanList entry);

    List<BanList> getBanLists();

    void removeBanList(BanList banList);
    
    void updateBanList(BanList banList);
    
    List<BanList> getBanLists(BanListFilter filter);

    Integer getTotalBanLists(BanListFilter filter);

	BanList getBanList(String name);


}
