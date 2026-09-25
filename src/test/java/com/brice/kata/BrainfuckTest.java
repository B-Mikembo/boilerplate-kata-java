package com.brice.kata;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BrainfuckTest {
  @Test
  void shouldReturnEmptyArrayWhenMissingProgram() {
    assertThat(execute("")).hasSize(30_000).containsOnly(0);
    assertThat(execute(null)).hasSize(30_000).containsOnly(0);
  }

  @Test
  void shouldIncrementByte() {
    byte[] execute = execute("+");
    assertThat(execute[0]).isEqualTo((byte) 1);
  }

  @Test
  void shouldIncrementMultipleTimes() {
    assertThat(execute("++")[0]).isEqualTo((byte) 2);
  }

  private byte[] execute(String program) {
    if(program == null || program.isBlank())
      return new byte[30_000];
    byte[] bytes = new byte[30_000];
    for(var character : program.toCharArray()) {
      bytes[0]++;
    }
    return bytes;
  }
}
