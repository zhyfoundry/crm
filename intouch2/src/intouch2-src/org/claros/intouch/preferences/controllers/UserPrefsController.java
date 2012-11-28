package org.claros.intouch.preferences.controllers;

import java.util.HashMap;
import java.util.List;

import org.claros.commons.auth.models.AuthProfile;
import org.claros.intouch.common.utility.Constants;
import org.claros.intouch.common.utility.Utility;
import org.claros.intouch.preferences.models.UserPreference;

import com.jenkov.mrpersister.impl.mapping.AutoGeneratedColumnsMapper;
import com.jenkov.mrpersister.itf.IGenericDao;
import com.jenkov.mrpersister.itf.mapping.IObjectMappingKey;
import com.jenkov.mrpersister.util.JdbcUtil;

/**
 * @author Umut Gokbayrak
 */
public class UserPrefsController {
	public UserPrefsController() {
		super();
	}

	/**
	 * Fetches all user preferences from DB.
	 * @param auth
	 * @return
	 * @throws Exception
	 */
	public static HashMap getUserPreferences(AuthProfile auth) throws Exception {
		HashMap prefMap = new HashMap();
		if (auth == null) {
			return prefMap;
		}
		IGenericDao dao = null;
		try {
			dao = Utility.getDbConnection();

			String username = auth.getUsername();
			String sql = "SELECT * FROM USER_PREFERENCES WHERE USERNAME=?";
			List prefs = dao.readList(UserPreference.class, sql, new Object[] {username});

			if (prefs != null) {
				UserPreference tmp = null;
				for (int i=0;i<prefs.size();i++) {
					tmp = (UserPreference)prefs.get(i);
					prefMap.put(tmp.getKeyword(), tmp.getPrefValue());
				}
			}
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return prefMap;
	}

	/**
	 * Retrieves a single preference setting from DB.
	 * @param auth
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getUserSetting(AuthProfile auth, String key) throws Exception {
		HashMap map = getUserPreferences(auth);
		return (String)map.get(key);
	}

	/**
	 * Saves a preference setting row into DB.
	 * @param auth
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void saveItem(AuthProfile auth, String key, String value) throws Exception {
		if (key == null || value == null || key.trim().equals("")) {
			return;
		}
		IGenericDao dao = null;
		try {
			dao = Utility.getDbConnection();

			String username = auth.getUsername();
			String sql = "SELECT * FROM USER_PREFERENCES WHERE USERNAME=? AND KEYWORD = ?";
			UserPreference pref = (UserPreference)dao.read(UserPreference.class, sql, new Object[] {username, key});
			if (pref == null) {
				// it is an insert
				pref = new UserPreference();
				pref.setKeyword((key == null) ? "" : key);
				pref.setUsername((username == null) ? "" : username);
				pref.setPrefValue((value == null) ? "" : value);

				IObjectMappingKey myObj = Constants.persistMan.getObjectMappingFactory().createInstance(UserPreference.class, new AutoGeneratedColumnsMapper(true));

				dao.insert(myObj, pref);
			} else {
				// it is an update
				pref.setKeyword(key);
				pref.setUsername(username);
				pref.setPrefValue(value);
				dao.update(pref);
			}
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
	}
}
