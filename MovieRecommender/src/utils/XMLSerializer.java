package utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class XMLSerializer implements Serializer
{

  @SuppressWarnings("rawtypes")
private Stack stack = new Stack();
  private File file;

  public XMLSerializer(File file)
  {
    this.file = file;
  }

  @SuppressWarnings("unchecked")
public void push(Object o)
  {
    stack.push(o);
  }

  public Object pop()
  {
    return stack.pop(); 
  }

  @SuppressWarnings({ "rawtypes" })
  public void read() throws Exception
  {
    ObjectInputStream is = null;

    try
    {
      is =  new ObjectInputStream(new GZIPInputStream(new BufferedInputStream ( new FileInputStream(file) ) ) );
      stack = (Stack) is.readObject();
    }
    finally
    {
      if (is != null)
      {
        is.close();
      }
    }
  }

  public void write() throws Exception
  {
    ObjectOutputStream os = null;

    try
    {
      os =  new ObjectOutputStream(new GZIPOutputStream(new BufferedOutputStream ( new FileOutputStream(file) ) ) );
      os.writeObject(stack);
    }
    finally
    {
      if (os != null)
      {
        os.close();
      }
    }
  }
}