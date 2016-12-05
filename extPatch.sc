+Patch {

	cvcGui { |displayDialog=true, prefix, pairs2D|
		var cDict = (), name;
		var thisType, thisControls, thisSpec, thisSlots, thisName, done=[];
		this.synthDef.allControlNames.do({ |cname| cDict.put(cname.name, cname.defaultValue) });
		if(displayDialog, {
			CVWidgetSpecsEditor(displayDialog, this, this.instr.asSymbol, cDict, prefix, pairs2D);
		}, {
			cDict.pairsDo({ |cName, vals|
				block { |break|
					pairs2D.pairsDo({ |wdgtName, cNames|
						if(cNames.includes(cName), {
							break.value(
								thisName = wdgtName;
								thisType = \w2dc;
								thisControls = cNames;
								thisSpec = cName.asSpec;
								done = done.add(cNames).flat;
							)
						})
					})
				};
				if(cDict[cName].size == 0 and:{ done.includes(cName).not }, { thisType = nil; thisName = cName });
				if(cDict[cName].size == 2, { thisType = \w2d; thisName = cName });
				if(cDict[cName].size > 2, { thisType = \wms; thisName = cName });

				switch(thisType,
					\w2dc, {
						thisSlots = [cDict[thisControls[0]], cDict[thisControls[1]]];
					},
					\w2d, {
						thisSlots = cDict[cName];
						thisSpec = cName.asSpec;
					},
					\wms, {
						thisSlots = cDict[cName];
						thisSpec = cName.asSpec;
					},
					{
						thisSlots = [cDict[cName]];
						thisSpec = cName.asSpec;
					}
				);

				prefix !? { thisName = prefix.asString++(thisName.asString[0]).toUpper ++ thisName.asString[1..] };

				// "this: %, this.synth: %\n".postf(this, this.synth);

				CVCenter.finishGui(this, cName, nil, (
					cName: thisName,
					type: thisType,
					enterTab: this.instr.asSymbol,
					controls: thisControls,
					slots: thisSlots,
					specSelect: thisSpec
				))
			})
		})
	}

}