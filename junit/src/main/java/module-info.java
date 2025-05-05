module com.asarkar.jdata.junit {
  requires transitive org.junit.jupiter.params;
  requires com.fasterxml.jackson.databind;
  requires static com.github.spotbugs.annotations;

  exports com.asarkar.junit;
}
