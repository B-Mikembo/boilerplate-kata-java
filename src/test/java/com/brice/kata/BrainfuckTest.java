package com.brice.kata;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class BrainfuckTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  void shouldReturnEmptyArrayWhenMissingProgram() {
    assertThat(execute("")).hasSize(30_000).containsOnly(0);
    assertThat(execute(null)).hasSize(30_000).containsOnly(0);
  }

  private byte[] execute(String program) {
    if (program == null || program.isBlank())
      return new byte[30_000];
    byte[] bytes = new byte[30_000];
    int dataPointer = 0;
    for (var character : program.toCharArray()) {
      switch (character) {
        case '+' -> bytes[dataPointer]++;
        case '-' -> bytes[dataPointer]--;
        case '>' -> dataPointer = (dataPointer + 1) % bytes.length;
        case '<' -> dataPointer = (dataPointer - 1) % bytes.length;
        default -> System.out.print((char) bytes[dataPointer]);
      }
    }
    return bytes;
  }

  @Test
  void shouldIncrement() {
    assertThat(execute("+")[0]).isEqualTo(toByte(1));
    assertThat(execute("++")[0]).isEqualTo(toByte(2));
  }

  private static byte toByte(int integer) {
    return (byte) integer;
  }

  @Test
  void shouldDecrement() {
    assertThat(execute("-")[0]).isEqualTo(toByte(255));
    assertThat(execute("--")[0]).isEqualTo(toByte(254));
  }

  //  [0,1,0,0,...]
  @Test
  void shouldIncrementDataPointer() {
    byte[] result = execute(">+");
    assertThat(result[0]).isEqualTo(toByte(0));
    assertThat(result[1]).isEqualTo(toByte(1));
  }

  @Test
  void shouldDecrementDataPointer() {
    byte[] result = execute("><+");
    assertThat(result[0]).isEqualTo(toByte(1));
    assertThat(result[1]).isEqualTo(toByte(0));
  }

  @Test
  void shouldPrintAsciiValueAtDataPointer() {
    execute("+".repeat(72) + ".");
    assertThat(outContent.toString().trim()).isEqualTo("H");
  }
}
