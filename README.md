## Usage
Download distribution from [here](https://github.com/KirillTim/verifier/releases/tag/v1).

```
java -jar verifier.jar
Usage: java -jar verifier.jar <path to XML file containing automation> <ltl formula>
```

### Tests
Threre are some tests in `/src/test`

### LTL formula grammar
LTL Parser is generated by ANTLR from [this grammar](https://github.com/KirillTim/verifier/blob/master/src/main/antlr/LTLFormula.g4).