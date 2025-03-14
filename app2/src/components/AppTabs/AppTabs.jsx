export default function AppTabs({ tabs, onTabSelected }) {
  const handleTabSelected = (index) => {
    if (typeof onTabSelected !== "undefined") {
      onTabSelected(index);
    }
  };

  return (
    <md-tabs>
      {tabs.map((tab, index) => (
        <Tab
          text={tab.title}
          icon={tab.icon}
          onClick={() => handleTabSelected(index)}
        />
      ))}
    </md-tabs>
  );
}

function Tab({ text, icon, onClick }) {
  return (
    <md-primary-tab onClick={onClick}>
        <md-icon>{icon}</md-icon>
        {text}
    </md-primary-tab>
  );
}
