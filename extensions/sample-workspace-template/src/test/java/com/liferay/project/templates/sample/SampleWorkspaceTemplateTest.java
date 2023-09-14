/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.project.templates.sample;

import com.liferay.blade.cli.BladeTest;
import com.liferay.blade.cli.TestUtil;

import java.io.File;
import java.io.FileInputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Christopher Bryan Boyd
 * @author Gregory Amerson
 */
public class SampleWorkspaceTemplateTest {

	@Before
	public void setUp() throws Exception {
		_rootDir = temporaryFolder.getRoot();

		_extensionsDir = temporaryFolder.newFolder(".blade", "extensions");
	}

	@Test
	public void testProjectTemplatesWithCustom() throws Exception {
		_setupTestExtensions();

		File tempDir = temporaryFolder.newFolder();

		String basePath = tempDir.getAbsolutePath();

		String[] args = {"--base", basePath, "init", "-P", "sample", "-v", BladeTest.PRODUCT_VERSION_PORTAL_74};

		TestUtil.runBlade(_rootDir, _extensionsDir, args);

		File settingsFile = new File(tempDir, "settings.gradle");

		Assert.assertTrue(settingsFile.exists());

		File bladePropertiesFile = new File(tempDir, ".blade.properties");

		Properties props = new Properties();

		try (FileInputStream fileInputStream = new FileInputStream(bladePropertiesFile)) {
			props.load(fileInputStream);

			String profileName = props.getProperty("profile.name");

			Assert.assertEquals("sample", profileName);
		}

		File gradleLocalPropertiesFile = new File(tempDir, "gradle-local.properties");

		Assert.assertTrue(gradleLocalPropertiesFile.exists());

		props = new Properties();

		try (FileInputStream fileInputStream = new FileInputStream(gradleLocalPropertiesFile)) {
			props.load(fileInputStream);

			String sampleSetting = props.getProperty("sample.setting");

			Assert.assertEquals("sample", sampleSetting);
		}
	}

	@Rule
	public final TemporaryFolder temporaryFolder = new TemporaryFolder();

	private void _setupTestExtension(Path extensionsPath, String jarPath) throws Exception {
		File sampleJarFile = new File(jarPath);

		Assert.assertTrue(sampleJarFile.getAbsolutePath() + " does not exist.", sampleJarFile.exists());

		Path sampleJarPath = extensionsPath.resolve(sampleJarFile.getName());

		Files.copy(sampleJarFile.toPath(), sampleJarPath, StandardCopyOption.REPLACE_EXISTING);

		Assert.assertTrue(Files.exists(sampleJarPath));
	}

	private void _setupTestExtensions() throws Exception {
		File extensionsDir = new File(temporaryFolder.getRoot(), ".blade/extensions");

		extensionsDir.mkdirs();

		Assert.assertTrue("Unable to create test extensions dir.", extensionsDir.exists());

		Path extensionsPath = extensionsDir.toPath();

		_setupTestExtension(extensionsPath, System.getProperty("sampleTemplateJarFile"));
	}

	private File _extensionsDir = null;
	private File _rootDir = null;

}