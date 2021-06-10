package io.mrs.web.components;

public interface Page {
	/**
	 * Get the page's header.
	 *
	 * @return an instance of Header that is suitable for this page
	 */
	Header getHeader();

	/**
	 * Get the page's footer.
	 *
	 * @return an instance of Footer that is suitable for this page
	 */
	Footer getFooter();
}
