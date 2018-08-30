package org.SirTobiSwobi.c3.classifiertrainer.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

import org.SirTobiSwobi.c3.classifiertrainer.db.Category;
import org.SirTobiSwobi.c3.classifiertrainer.db.CategoryManager;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class TCCategoriesTest {
	
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void serializesToJSON() throws Exception {
		
		CategoryManager catMan =  new CategoryManager();
		catMan.addCategory(new Category(0,"first category","first description"));
		catMan.addCategory(new Category(1,"second category","second description"));
		catMan.addCategory(new Category(2,"third category","third description"));
		Category[] categories = catMan.getCategoryArray();
		TCCategory[] TCcategoryArray = new TCCategory[categories.length];
		for(int i=0; i<categories.length;i++){
			Category cat = categories[i];
			TCCategory TCcat = new TCCategory(cat.getId(),cat.getLabel(),cat.getDescription());
			TCcategoryArray[i]=TCcat;
		}
		TCCategories TCcategories = new TCCategories(TCcategoryArray);
	
		final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/TCCategories.json"), TCCategories.class));
		
		assertThat(MAPPER.writeValueAsString(TCcategories)).isEqualTo(expected);
		
	}

}
