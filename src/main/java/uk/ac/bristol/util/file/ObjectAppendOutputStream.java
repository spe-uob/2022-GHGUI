package uk.ac.bristol.util.file;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * A custom ObjectOutputStream that allows objects to be appended to an existing file. This class
 * overrides the writeStreamHeader() method to prevent the header from being written between
 * objects, so that objects can be written continuously to the same file.
 */
class ObjectAppendOutputStream extends ObjectOutputStream {

  /**
   * Creates a new ObjectAppendOutputStream that writes to an underlying OutputStream.
   *
   * @param out the underlying OutputStream to write to
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public ObjectAppendOutputStream(OutputStream out) throws IOException {
    super(out);
  }

  /**
   * Overrides the default ObjectOutputStream behavior to prevent the serialization header from
   * being written between objects. This allows objects to be written continuously to the same file.
   */
  @Override
  protected void writeStreamHeader() throws IOException {
    // Do nothing; we don't want to write the header
  }
}
