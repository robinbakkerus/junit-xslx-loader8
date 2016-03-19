# junit-xslx-loader8

This is a (Java8) library that makes it very easy to populate in your junit tests, the objects with testdata coming from Excel(2007) **.xlsx** files.
(this projec is identical to junit-xlst-loader7 but in addition also supports java.util.LocalDate(Time) )


# Setup instruction
In the coming weeks I want to post the jar file to the maven-repository. For now do the following:

* Simply clone or download this project. 
* Run: mvn package
* Add the generated jar file to project. 

This project depends on:

		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-ooxml</artifactId>
			<version>3.9</version>
		</dependency>
		<dependency>
			<groupId>joda-time</groupId>
			<artifactId>joda-time</artifactId>
			<version>2.9.2</version>
		</dependency>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
		</dependency>

# Usage and documentation

Goto the website:  http://robinbakkerus.github.io/junit-xslx-loader8

