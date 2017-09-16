package evolution;

import java.util.List;

public class CodeWriter {
	private int indentCount;
	
	public void writeIndent(StringBuilder code) {
		for (int i = 0; i < indentCount; i++) {
			for (int j = 0; j < 4; j++) {
				code.append(" ");
			}
		}
	}
	
	public void writeCode(String code, StringBuilder codes) {
		String trimedCode = code.trim();
		if (trimedCode.endsWith("{")) {
			writeIndent(codes);
			indentCount++;
		} else if (trimedCode.endsWith("}")) {
			indentCount--;
			writeIndent(codes);
		} else {
			writeIndent(codes);
		}
		codes.append(trimedCode).append("\n");
	}
	
	public void writeCodes(List<String> codesToBeWritten, StringBuilder codes) {
		for (String codeToBeWritten : codesToBeWritten) {
			writeCode(codeToBeWritten, codes);
		}
	}
}
