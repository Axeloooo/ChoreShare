import { useState } from 'react';
import Select from 'react-select';
import '../styles/CreateChore.css';

const frequencyOptions = [
    { value: 'week-once', label: 'Once a week', category: 'frequency' },
    { value: 'week-twice', label: 'Twice a week', category: 'frequency' },
    { value: 'every-day', label: 'Every day', category: 'frequency'  },
    { value: 'every-other-day', label: 'Every other day', category: 'frequency'  },
];

const tagOptions = [
    { value: 'kitchen', label: 'Kitchen', category: 'tag' },
    { value: 'bathroom', label: 'Bathroom', category: 'tag' },
    { value: 'general', label: 'General', category: 'tag'},
];

const groupedOptions = [
    {
      label: 'Frequencies',
      options: frequencyOptions,
    },
    {
      label: 'Labels',
      options: tagOptions,
    },
  ];

const groupStyles = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
  };

const groupBadgeStyles = {
    backgroundColor: '#EBECF0',
    borderRadius: '2em',
    color: '#172B4D',
    display: 'inline-block',
    fontSize: 12,
    fontWeight: 'normal',
    lineHeight: '1',
    minWidth: 1,
    padding: '0.16666666666667em 0.5em',
    textAlign: 'center',
  };

  const formatGroupLabel = data => (
    <div style={groupStyles}>
      <span>{data.label}</span>
      <span style={groupBadgeStyles}>{data.options.length}</span>
    </div>
  );

function CreateChore(){
    const [selectedOptions, setSelectedOptions] = useState([]);
    
    const handleChange = (selected) => {
        const lastAddedOption = selected[selected.length - 1];

        if (lastAddedOption && selected.length >= selectedOptions.length){
            const filteredOptions = selectedOptions.filter(option => {
                return option.category !== lastAddedOption.category;
            })

            const newSelectedOptions = [...filteredOptions, lastAddedOption];

            setSelectedOptions(newSelectedOptions);
        } else {
            setSelectedOptions(selected);
        }
      };

    return (
        <div className="create-chore-window">
            <h1>Add a chore</h1>
            <label htmlFor="chore-tags" className="label">Tags</label>
            <Select
                className="select"
                id="chore-tags"
                isMulti
                options={groupedOptions}
                formatGroupLabel={formatGroupLabel}
                onChange={handleChange}
                value={selectedOptions}
                styles={{
                  control: (base) => ({
                    ...base,
                    fontSize: '0.8rem',
                  }),
                  menu: (base) => ({
                    ...base,
                    fontSize: '0.8rem',
                  }),
                  option: (base) => ({
                    ...base,
                    fontSize: '0.8rem',
                  }),
                }}
            />
            <form className="create-chore-form">
                <label htmlFor="chore-title">Title</label>
                <input type="text" id="chore-title" name="chore-title" />
                <label htmlFor="chore-description">Description (optional)</label>
                <textarea id="chore-description" name="chore-description" />
                <button type="submit">Create Chore</button>
            </form>
        </div>
    )
}

export default CreateChore;

