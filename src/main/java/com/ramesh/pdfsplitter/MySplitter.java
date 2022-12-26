package com.ramesh.pdfsplitter;

public class MySplitter {

		private Integer startPage;
		private Integer endPage;
		private Integer splitAt;
		private String fileNameAppender;
		private String folder;
		
		private String splitSearchText;
		
			

		public MySplitter(String splitSearchText, String fileNameAppender, String folder) {
			this.splitSearchText = splitSearchText;
			this.fileNameAppender = fileNameAppender;
			this.folder = folder;
		}

		public Integer getStartPage() {
			return startPage;
		}

		public void setStartPage(Integer startPage) {
			this.startPage = startPage;
		}

		public Integer getEndPage() {
			return endPage;
		}

		public void setEndPage(Integer endPage) {
			this.endPage = endPage;
		}

		public Integer getSplitAt() {
			return splitAt;
		}

		public void setSplitAt(Integer splitAt) {
			this.splitAt = splitAt;
		}

		public String getSplitSearchText() {
			return splitSearchText;
		}

		public void setSplitSearchText(String splitSearchText) {
			this.splitSearchText = splitSearchText;
		}

		public String getFileNameAppender() {
			return fileNameAppender;
		}

		public void setFileNameAppender(String fileNameAppender) {
			this.fileNameAppender = fileNameAppender;
		}

		public String getFolder() {
			return folder;
		}

		public void setFolder(String folder) {
			this.folder = folder;
		}
		
		
}
