package com.smoothstack.transactionbatch.writer;

// THIS ORIGINAL SOURCE CODE HAS BEEN EDITED

/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Map;

import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.util.Assert;

/**
 * A builder for the {@link SynchronizedXML}.
 *
 * @author Michael Minella
 * @author Parikshit Dutta
 * @author Mahmoud Ben Hassine
 * @since 4.0
 * @see SynchronizedXML
 */
public class SynchronizedXMLBuilder<T> {

	private Resource resource;

	private Marshaller marshaller;

	private StaxWriterCallback headerCallback;

	private StaxWriterCallback footerCallback;

	private boolean transactional = true;

	private boolean forceSync = false;

	private boolean shouldDeleteIfEmpty = false;

	private String encoding = SynchronizedXML.DEFAULT_ENCODING;

	private String version = SynchronizedXML.DEFAULT_XML_VERSION;

	private Boolean standalone = SynchronizedXML.DEFAULT_STANDALONE_DOCUMENT;

	private String rootTagName = SynchronizedXML.DEFAULT_ROOT_TAG_NAME;

	private Map<String, String> rootElementAttributes;

	private boolean overwriteOutput = true;

	private boolean saveState = true;

	private String name;

	/**
	 * The name used to calculate the key within the
	 * {@link org.springframework.batch.item.ExecutionContext}.  Required if
	 * {@link SynchronizedXMLBuilder#saveState(boolean)} is set to true.
	 *
	 * @param name name of the reader instance
	 * @return The current instance of the builder.
	 * @see SynchronizedXML#setName(String)
	 */
	public SynchronizedXMLBuilder<T> name(String name) {
		this.name = name;

		return this;
	}

	/**
	 * The {@link Resource} to be used as output.
	 *
	 * @param resource the output from the writer
	 * @return the current instance of the builder.
	 * @see SynchronizedXML#setResource(Resource)
	 */
	public SynchronizedXMLBuilder<T> resource(Resource resource) {
		this.resource = resource;

		return this;
	}

	/**
	 * The {@link Marshaller} implementation responsible for the serialization of the
	 * items to XML.  This field is required.
	 *
	 * @param marshaller the component used to generate XML
	 * @return the current instance of the builder.
	 * @see SynchronizedXML#setMarshaller(Marshaller)
	 */
	public SynchronizedXMLBuilder<T> marshaller(Marshaller marshaller) {
		this.marshaller = marshaller;

		return this;
	}

	/**
	 * A {@link StaxWriterCallback} to provide any header elements
	 *
	 * @param headerCallback a {@link StaxWriterCallback}
	 * @return the current instance of the builder.
	 * @see SynchronizedXML#setHeaderCallback(StaxWriterCallback)
	 */
	public SynchronizedXMLBuilder<T> headerCallback(StaxWriterCallback headerCallback) {
		this.headerCallback = headerCallback;

		return this;
	}

	/**
	 * A {@link StaxWriterCallback} to provide any footer elements
	 *
	 * @param footerCallback a {@link StaxWriterCallback}
	 * @return the current instance of the builder.
	 * @see SynchronizedXML#setFooterCallback(StaxWriterCallback)
	 */
	public SynchronizedXMLBuilder<T> footerCallback(StaxWriterCallback footerCallback) {
		this.footerCallback = footerCallback;

		return this;
	}

	/**
	 * The resulting writer is participating in a transaction and writes should be delayed
	 * as late as possible.
	 *
	 * @param transactional indicates that the writer is transactional.  Defaults to false.
	 * @return the current instance of the builder
	 * @see SynchronizedXML#setTransactional(boolean)
	 */
	public SynchronizedXMLBuilder<T> transactional(boolean transactional) {
		this.transactional = transactional;

		return this;
	}

	/**
	 * Flag to indicate that changes should be force-synced to disk on flush.
	 *
	 * @param forceSync indicates if force sync should occur.  Defaults to false.
	 * @return the current instance of the builder
	 * @see SynchronizedXML#setForceSync(boolean)
	 */
	public SynchronizedXMLBuilder<T> forceSync(boolean forceSync) {
		this.forceSync = forceSync;

		return this;
	}

	/**
	 * Flag to indicate that the output file should be deleted if no results were written
	 * to it.  Defaults to false.
	 *
	 * @param shouldDelete indicator
	 * @return the current instance of the builder
	 * @see SynchronizedXML#setShouldDeleteIfEmpty(boolean)
	 */
	public SynchronizedXMLBuilder<T> shouldDeleteIfEmpty(boolean shouldDelete) {
		this.shouldDeleteIfEmpty = shouldDelete;

		return this;
	}

	/**
	 * Encoding for the file.  Defaults to UTF-8.
	 *
	 * @param encoding String encoding algorithm
	 * @return the current instance of the builder
	 * @see SynchronizedXML#setEncoding(String)
	 */
	public SynchronizedXMLBuilder<T> encoding(String encoding) {
		this.encoding = encoding;

		return this;
	}

	/**
	 * Version of XML to be generated.  Must be supported by the {@link Marshaller}
	 * provided.
	 *
	 * @param version XML version
	 * @return the current instance of the builder
	 * @see SynchronizedXML#setVersion(String)
	 */
	public SynchronizedXMLBuilder<T> version(String version) {
		this.version = version;

		return this;
	}

	/**
	 * Standalone document declaration for the output document. Defaults to {@code null}.
	 *
	 * @param standalone Boolean standalone document declaration
	 * @return the current instance of the builder
	 * @see SynchronizedXML#setStandalone(Boolean)
	 *
	 * @since 4.3
	 */
	public SynchronizedXMLBuilder<T> standalone(Boolean standalone) {
		this.standalone = standalone;

		return this;
	}

	/**
	 * The name of the root tag for the output document.
	 *
	 * @param rootTagName tag name
	 * @return the current instance of the builder
	 * @see SynchronizedXML#setRootTagName(String)
	 */
	public SynchronizedXMLBuilder<T> rootTagName(String rootTagName) {
		this.rootTagName = rootTagName;

		return this;
	}

	/**
	 * A Map of attributes to be included in the document's root element.
	 *
	 * @param rootElementAttributes map fo attributes
	 * @return the current instance of the builder.
	 * @see SynchronizedXML#setRootElementAttributes(Map)
	 */
	public SynchronizedXMLBuilder<T> rootElementAttributes(Map<String, String> rootElementAttributes) {
		this.rootElementAttributes = rootElementAttributes;

		return this;
	}

	/**
	 * Indicates if an existing file should be overwritten if found. Defaults to true.
	 *
	 * @param overwriteOutput indicator
	 * @return the current instance of the builder.
	 * @see SynchronizedXML#setOverwriteOutput(boolean)
	 */
	public SynchronizedXMLBuilder<T> overwriteOutput(boolean overwriteOutput) {
		this.overwriteOutput = overwriteOutput;

		return this;
	}

	/**
	 * Indicates if the state of the writer should be saved in the
	 * {@link org.springframework.batch.item.ExecutionContext}.  Setting this to false
	 * will impact restartability.  Defaults to true.
	 *
	 * @param saveState indicator
	 * @return the current instance of the builder
	 * @see SynchronizedXML#setSaveState(boolean)
	 */
	public SynchronizedXMLBuilder<T> saveState(boolean saveState) {
		this.saveState = saveState;

		return this;
	}

	/**
	 * Returns a configured {@link SynchronizedXML}
	 *
	 * @return a SynchronizedXML
	 */
	public SynchronizedXML<T> build() {
		Assert.notNull(this.marshaller, "A marshaller is required");

		if(this.saveState) {
			Assert.notNull(this.name, "A name is required");
		}

		SynchronizedXML<T> writer = new SynchronizedXML<>();

		writer.setEncoding(this.encoding);
		writer.setFooterCallback(this.footerCallback);
		writer.setForceSync(this.forceSync);
		writer.setHeaderCallback(this.headerCallback);
		writer.setMarshaller(this.marshaller);
		writer.setOverwriteOutput(this.overwriteOutput);
		writer.setResource(this.resource);
		writer.setRootElementAttributes(this.rootElementAttributes);
		writer.setRootTagName(this.rootTagName);
		writer.setSaveState(this.saveState);
		writer.setShouldDeleteIfEmpty(this.shouldDeleteIfEmpty);
		writer.setTransactional(this.transactional);
		writer.setVersion(this.version);
		writer.setName(this.name);
		writer.setStandalone(this.standalone);

		return writer;
	}

 }
