/**
 * This is a tutorial source code 
 * provided "as is" and without warranties.
 *
 * For any question please visit the web site
 * http://www.survivingwithandroid.com
 *
 * or write an email to
 * survivingwithandroid@gmail.com
 *
 */
package com.weemscreekstudios.smartmirror1.model;

/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Weather {
	
	public Location location;
	public CurrentCondition currentCondition = new CurrentCondition();
	public Temperature temperature = new Temperature();
	public Wind wind = new Wind();
	public Rain rain = new Rain();
	public Snow snow = new Snow()	;
	public Clouds clouds = new Clouds();
	public long dt;

	
	public byte[] iconData;

	public void setdt(long dtIn) {dt = dtIn;  }
	public long getdt() {return dt;}


	public  class CurrentCondition {
		private int weatherId;
		private String condition;
		private String descr;
		private String icon;
		
		
		private float pressure;
		private float humidity;

		//private long dt;
		
		public int getWeatherId() {
			return weatherId;
		}
		public void setWeatherId(int weatherId) {
			this.weatherId = weatherId;
		}
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public String getDescr() {
			return descr;
		}
		public void setDescr(String descr) {
			this.descr = descr;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public float getPressure() {
			return pressure;
		}
		public float getPressureImperial() {
			float pressureImp = pressure/38.63886666718317f;
			return pressureImp;
		}
		public void setPressure(float pressure) {
			this.pressure = pressure;
		}
		public float getHumidity() {
			return humidity;
		}
		public void setHumidity(float humidity) {
			this.humidity = humidity;
		}

		
	}
	
	public  class Temperature {
		private float temp;
		private float minTemp;
		private float maxTemp;
		
		public float getTemp() {
			return temp;
		}
		public void setTemp(float temp) {
			this.temp = temp;
		}
		public float getMinTemp() {
			return minTemp;
		}
		public void setMinTemp(float minTemp) {
			this.minTemp = minTemp;
		}
		public float getMaxTemp() {
			return maxTemp;
		}
		public void setMaxTemp(float maxTemp) {
			this.maxTemp = maxTemp;
		}
		
	}
	
	public  class Wind {
		private float speed;
		private float deg;
		public float getSpeed() {
			return speed;
		}
		public void setSpeed(float speed) {
			this.speed = speed;
		}
		public float getDeg() {
			return deg;
		}
		public void setDeg(float deg) {
			this.deg = deg;
		}
		
		
	}
	
	public  class Rain {
		private String time;
		private float ammount;
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public float getAmmount() {
			return ammount;
		}
		public void setAmmount(float ammount) {
			this.ammount = ammount;
		}
		
		
		
	}

	public  class Snow {
		private String time;
		private float ammount;
		
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public float getAmmount() {
			return ammount;
		}
		public void setAmmount(float ammount) {
			this.ammount = ammount;
		}
		
		
	}
	
	public  class Clouds {
		private int perc;

		public int getPerc() {
			return perc;
		}

		public void setPerc(int perc) {
			this.perc = perc;
		}
		
		
	}

	@Override
	public String toString() {
		/*return "OpenWeatherMapAPI_Single_City [weather=" + weather + ", temp=" + temp + ", wind="
				+ wind + ", clouds=" + clouds + ", rain=" + rain + ", dt=" + dt + ", id=" + id +", cod=" + cod + "]";  */
		return "nothing parsed yet";
	}

}
