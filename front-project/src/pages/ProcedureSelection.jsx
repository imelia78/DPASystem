import { useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { PROCEDURE_TYPES } from '../config/constants';
import { ChevronDown, Clock, Heart, Droplet, Brain, Baby, Stethoscope, Bone, Eye, Smile } from 'lucide-react';
import { Button } from '../components/ui/Button';
import { useTranslation } from 'react-i18next';

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 3rem;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
`;

const Header = styled.div`
  margin-bottom: 1rem;
  h1 {
    font-size: 2rem;
    font-weight: 800;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.5rem;
  }
  p {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 1.1rem;
  }
`;

const GroupSection = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

const GroupTitle = styled.h2`
  font-size: 1.25rem;
  font-weight: 700;
  color: ${({ theme }) => theme.colors.text};
  display: flex;
  align-items: center;
  gap: 0.75rem;

  &::before {
    content: '';
    display: inline-block;
    width: 4px;
    height: 24px;
    background: ${({ theme }) => theme.colors.secondary};
    border-radius: 4px;
  }
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 1.5rem;
  align-items: flex-start;
`;

const Card = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  transition: ${({ theme }) => theme.transitions.default};
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.02);

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 10px 25px -5px rgba(0,0,0,0.05);
    border-color: ${({ theme }) => theme.colors.primary}40;
  }
`;

const CardHeader = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
`;

const IconWrapper = styled.div`
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: ${({ $color }) => `${$color}15`};
  color: ${({ $color }) => $color};
  display: flex;
  align-items: center;
  justify-content: center;
`;

const CategoryInfo = styled.div`
  h3 {
    font-size: 1.15rem;
    font-weight: 700;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.25rem;
  }
  p {
    font-size: 0.9rem;
    color: ${({ theme }) => theme.colors.textMuted};
    line-height: 1.4;
  }
`;

const DurationLabel = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.85rem;
  color: ${({ theme }) => theme.colors.textMuted};
  font-weight: 500;
`;

const SubspecialtyDropdown = styled.div`
  border-top: 1px solid ${({ theme }) => theme.colors.border};
  padding-top: 1rem;
`;

const DropdownToggle = styled.button`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: transparent;
  border: none;
  color: ${({ theme }) => theme.colors.secondary};
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  padding: 0.5rem 0;
  
  svg {
    transition: transform 0.3s ease;
    transform: ${({ $isOpen }) => $isOpen ? 'rotate(180deg)' : 'rotate(0)'};
  }
`;

const SubtypeList = styled.div`
  display: ${({ $isOpen }) => ($isOpen ? 'flex' : 'none')};
  flex-direction: column;
  gap: 0.5rem;
  margin-top: 1rem;
`;

const SubtypeItem = styled.div`
  padding: 0.75rem 1rem;
  border-radius: ${({ theme }) => theme.radii.sm};
  background: ${({ theme }) => theme.colors.surfaceHover};
  font-size: 0.9rem;
  color: ${({ theme }) => theme.colors.text};
  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.8rem;
  }
`;

// Helper data for visual mapping
const categoryMeta = {
  'Cardiology': { icon: Heart, color: '#ef4444', desc: 'Heart and cardiovascular system specialists', time: '~30 min' },
  'Dermatology': { icon: Droplet, color: '#f472b6', desc: 'Skin, hair, and nail care experts', time: '~20 min' },
  'Neurology': { icon: Brain, color: '#d946ef', desc: 'Brain and nervous system specialists', time: '~45 min' },
  'Pediatrics': { icon: Baby, color: '#eab308', desc: 'Child health specialists', time: '~25 min' },
  'General Medicine': { icon: Stethoscope, color: '#8b5cf6', desc: 'General health and wellness', time: '~20 min' },
  'Orthopedics': { icon: Bone, color: '#94a3b8', desc: 'Bone and joint specialists', time: '~30 min' },
  'Ophthalmology': { icon: Eye, color: '#3b82f6', desc: 'Eye care specialists', time: '~25 min' },
  'Dentistry': { icon: Smile, color: '#14b8a6', desc: 'Oral health and dental care', time: '~30 min' },
};

const medicalSpecialties = ['Cardiology', 'Dermatology', 'Neurology', 'Pediatrics', 'General Medicine', 'Orthopedics', 'Ophthalmology', 'Dentistry'];

const ProcedureSelection = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [openDropdowns, setOpenDropdowns] = useState({});

  const toggleDropdown = (category) => {
    setOpenDropdowns(prev => ({ ...prev, [category]: !prev[category] }));
  };



  const handleFindDoctors = (category) => {
    // In a real app, this would route to all doctors in the category
    navigate(`/patient/doctors?category=${encodeURIComponent(category)}`);
  };

  const renderGroup = (title, categories) => {
    const matchedProcedures = PROCEDURE_TYPES.filter(p => categories.includes(p.category));
    
    if (matchedProcedures.length === 0) return null;

    return (
      <GroupSection>
        <GroupTitle>{title}</GroupTitle>
        <Grid>
          {matchedProcedures.map((proc) => {
            const meta = categoryMeta[proc.category] || { icon: Stethoscope, color: '#10b981', desc: 'Specialist consultation', time: '~30 min' };
            const IconComponent = meta.icon;
            const isOpen = !!openDropdowns[proc.category];

            return (
              <Card key={proc.category}>
                <CardHeader>
                  <IconWrapper $color={meta.color}>
                    <IconComponent size={24} />
                  </IconWrapper>
                  <CategoryInfo>
                    <h3>{t(`procedure.categories.${proc.category}`, proc.category)}</h3>
                    <p>{t(`procedure.descriptions.${proc.category}`, meta.desc)}</p>
                  </CategoryInfo>
                  <DurationLabel>
                    <Clock size={16} /> {meta.time} {t('procedure.consultation')}
                  </DurationLabel>
                </CardHeader>

                <SubspecialtyDropdown>
                  <DropdownToggle $isOpen={isOpen} onClick={() => toggleDropdown(proc.category)}>
                    {t('procedure.subSpecialties')} ({proc.subtypes.length})
                    <ChevronDown size={18} />
                  </DropdownToggle>
                  
                  <SubtypeList $isOpen={isOpen}>
                    {proc.subtypes.map(subtype => (
                      <SubtypeItem key={subtype.id}>
                        {t(`procedure.subtypes.${subtype.id}`, { defaultValue: subtype.name })}
                        <span>{subtype.duration}m</span>
                      </SubtypeItem>
                    ))}
                  </SubtypeList>
                </SubspecialtyDropdown>

                <Button 
                  fullWidth 
                  style={{ marginTop: 'auto' }}
                  onClick={() => handleFindDoctors(proc.category)}
                >
                  {t('procedure.findDoctors')} &rarr;
                </Button>
              </Card>
            );
          })}
        </Grid>
      </GroupSection>
    );
  };

  return (
    <PageContainer>
      <Header>
        <h1>{t('procedure.title')}</h1>
        <p>{t('procedure.subtitle')}</p>
      </Header>

      {renderGroup(t('procedure.medicalSpecialties'), medicalSpecialties)}

    </PageContainer>
  );
};

export default ProcedureSelection;
