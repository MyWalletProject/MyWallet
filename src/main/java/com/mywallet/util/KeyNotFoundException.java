package com.mywallet.util;

public class KeyNotFoundException extends RuntimeException {
	 /**
		 * defaut serial id
		 */
		private static final long serialVersionUID = 1L;
		
		public KeyNotFoundException(String message) {
			super(message);
	    }		
		
}
