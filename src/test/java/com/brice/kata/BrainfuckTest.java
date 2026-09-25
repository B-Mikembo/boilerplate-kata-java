package com.brice.kata;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BrainfuckTest {
  @Test
  void shouldReturnEmptyArrayWhenMissingProgram() {
    assertThat(execute("")).hasSize(30_000).containsOnly(0);
    assertThat(execute(null)).hasSize(30_000).containsOnly(0);
  }

  private byte[] execute(String program) {
    return new byte[30_000];
  }
}
