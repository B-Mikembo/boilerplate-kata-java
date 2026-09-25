package com.brice.kata;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class BrainfuckTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;
  private ByteArrayInputStream inContent;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
    System.setIn(originalIn);
  }

  @Test
  void shouldReturnEmptyArrayWhenMissingProgram() throws IOException {
    assertThat(execute("")).hasSize(30_000).containsOnly(0);
    assertThat(execute(null)).hasSize(30_000).containsOnly(0);
  }

  private byte[] execute(String program) throws IOException {
    if (program == null || program.isBlank())
      return new byte[30_000];
    byte[] bytes = new byte[30_000];
    int dataPointer = 0;
    Scanner in = new Scanner(System.in);
    boolean skipped = false;
    for (var character : program.toCharArray()) {
      if(skipped) continue;
      switch (character) {
        case '+' -> bytes[dataPointer]++;
        case '-' -> bytes[dataPointer]--;
        case '>' -> dataPointer = (dataPointer + 1) % bytes.length;
        case '<' -> dataPointer = (dataPointer - 1) % bytes.length;
        case ',' -> bytes[dataPointer] = (byte) System.in.read();
        case '[' -> skipped = true;
        case ']' -> skipped = false;
        default -> System.out.print((char) bytes[dataPointer]);
      }
    }
    return bytes;
  }

  @Test
  void shouldIncrement() throws IOException {
    assertThat(execute("+")[0]).isEqualTo(toByte(1));
    assertThat(execute("++")[0]).isEqualTo(toByte(2));
  }

  private static byte toByte(int integer) {
    return (byte) integer;
  }

  @Test
  void shouldDecrement() throws IOException {
    assertThat(execute("-")[0]).isEqualTo(toByte(255));
    assertThat(execute("--")[0]).isEqualTo(toByte(254));
  }

  //  [0,1,0,0,...]
  @Test
  void shouldIncrementDataPointer() throws IOException {
    byte[] result = execute(">+");
    assertThat(result[0]).isEqualTo(toByte(0));
    assertThat(result[1]).isEqualTo(toByte(1));
  }

  @Test
  void shouldDecrementDataPointer() throws IOException {
    byte[] result = execute("><+");
    assertThat(result[0]).isEqualTo(toByte(1));
    assertThat(result[1]).isEqualTo(toByte(0));
  }

  @Test
  void shouldPrintAsciiValueAtDataPointer() throws IOException {
    execute("+".repeat(72) + ".");
    assertThat(outContent.toString().trim()).isEqualTo("H");
  }

  @Test
  void shouldInsertByteFromAsciiInputAtDataPointer() throws IOException {
    inContent = new ByteArrayInputStream("H".getBytes());
    System.setIn(inContent);
    byte[] result = execute(",");
    assertThat(result[0]).isEqualTo(toByte(72));
  }

  @Test
  void shouldIgnoreCommandsBetweenBracketsWhenByteAtDataPointerEqualsZero() throws IOException {
    byte[] bytes = execute("[" + "+".repeat(72) + "]");
    assertThat(bytesToString(bytes)).isEqualTo("");
  }

  private static String bytesToString(byte[] bytes) {
    return new String(bytes).trim();
  }
}
