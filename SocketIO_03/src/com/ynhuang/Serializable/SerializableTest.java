package com.ynhuang.Serializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableTest {// implements Serializable {
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args) {
		SerializableTest test = new SerializableTest();
		test.setId(1);
		test.setName("ynhuang");

		try {
			// 序列化过程
			FileOutputStream fos = new FileOutputStream("/test.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(test);
			oos.flush();
			oos.close();
			fos.close();
			// 反序列化过程
			FileInputStream fis = new FileInputStream("/test.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			SerializableTest test1 = (SerializableTest) ois.readObject();
			System.out.println("id = " + test1.getId());
			System.out.println("name = " + test1.name);
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
