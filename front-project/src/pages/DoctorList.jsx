import { useState, useMemo, useEffect } from 'react';
import styled from 'styled-components';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { doctorService } from '../services/api';
import { Search, Filter, Star, MessageSquare, Info } from 'lucide-react';
import { Input, Select } from '../components/ui/Input';
import { Button } from '../components/ui/Button';
import { useTranslation } from 'react-i18next';

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  
  h1 {
    font-size: 2rem;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.5rem;
  }
  p {
    color: ${({ theme }) => theme.colors.primary};
    font-weight: 500;
  }
`;

const ControlsContainer = styled.div`
  display: flex;
  gap: 1rem;
  background: ${({ theme }) => theme.colors.surface};
  padding: 1.5rem;
  border-radius: ${({ theme }) => theme.radii.lg};
  border: 1px solid ${({ theme }) => theme.colors.border};
  align-items: center;
  flex-wrap: wrap;
`;

const SearchBox = styled.div`
  flex: 1;
  min-width: 300px;
  position: relative;
  
  svg {
    position: absolute;
    left: 1rem;
    top: 50%;
    transform: translateY(-50%);
    color: ${({ theme }) => theme.colors.textMuted};
  }
  
  input {
    padding-left: 3rem;
  }
`;

const CheckboxLabel = styled.label`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  color: ${({ theme }) => theme.colors.text};
  user-select: none;
  
  input {
    accent-color: ${({ theme }) => theme.colors.primary};
    width: 1.2rem;
    height: 1.2rem;
    cursor: pointer;
  }
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
`;

const DoctorCard = styled.div`
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 1.5rem 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  transition: ${({ theme }) => theme.transitions.default};
  position: relative;

  &:hover {
    box-shadow: ${({ theme }) => theme.shadows.card};
  }
`;

const RatingPill = styled.div`
  position: absolute;
  top: 1.5rem;
  right: 2rem;
  background: #fef9c3;
  color: #854d0e;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-weight: 600;
  font-size: 0.9rem;
`;

const NameSection = styled.div`
  h3 {
    font-size: 1.5rem;
    color: ${({ theme }) => theme.colors.text};
    margin-bottom: 0.25rem;
    font-weight: 600;
  }
  .specialty {
    color: #2563eb;
    font-size: 1rem;
    font-weight: 500;
  }
`;

const InfoGrid = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-top: 0.5rem;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }

  div {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    color: ${({ theme }) => theme.colors.textMuted};
    font-size: 0.95rem;

    svg {
      color: #94a3b8;
    }
  }
`;

const ActionRow = styled.div`
  display: flex;
  gap: 1rem;
  margin-top: 0.5rem;
  align-items: center;
  flex-wrap: wrap;
`;

const AvailableBadge = styled.div`
  background: #dcfce7;
  color: #166534;
  padding: 0.35rem 1rem;
  border-radius: 9999px;
  font-size: 0.85rem;
  font-weight: 600;
  display: inline-block;
  text-align: center;
`;

const DoctorList = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { t } = useTranslation();
  const subtype = searchParams.get('subtype');
  const category = searchParams.get('category');

  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [availableWithin2Weeks, setAvailableWithin2Weeks] = useState(false);
  const [sortOption, setSortOption] = useState('rating');

  useEffect(() => {
    const fetchDoctors = async () => {
      try {
        const response = await doctorService.getAll(100, 0);
        setDoctors(response.data);
      } catch (err) {
        console.error("Failed to fetch doctors", err);
      } finally {
        setLoading(false);
      }
    };
    fetchDoctors();
  }, []);

  const filteredDoctors = useMemo(() => {
    let result = doctors;

    // Filter by procedure subtype or category if specified
    if (subtype) {
      result = result.filter(doc => doc.specialization?.toLowerCase().includes(subtype.toLowerCase()));
    } else if (category) {
      result = result.filter(doc => doc.specialization?.toLowerCase().includes(category.toLowerCase()));
    }

    // Search query filter
    if (searchQuery) {
      const lowerQ = searchQuery.toLowerCase();
      result = result.filter(doc => 
        doc.firstName?.toLowerCase().includes(lowerQ) ||
        doc.lastName?.toLowerCase().includes(lowerQ) ||
        (doc.specialization || '').toLowerCase().includes(lowerQ)
      );
    }

    // Sorting
    result.sort((a, b) => {
      if (sortOption === 'rating') {
        const avgA = a.averageRating || 0;
        const avgB = b.averageRating || 0;
        return avgB - avgA;
      }
      if (sortOption === 'name-asc') return (a.lastName || '').localeCompare(b.lastName || '');
      if (sortOption === 'name-desc') return (b.lastName || '').localeCompare(a.lastName || '');
      return 0;
    });

    return result;
  }, [doctors, subtype, category, searchQuery, availableWithin2Weeks, sortOption]);

  const handleDoctorClick = (docId) => {
    navigate(`/patient/doctor/${docId}`);
  };

  return (
    <PageContainer>
      <Header>
        <div>
          <h1>{t('doctorList.title')}</h1>
          {(subtype || category) && <p>{t('doctorList.showingSpecialists')} {subtype || category}</p>}
        </div>
      </Header>

      <ControlsContainer>
        <SearchBox>
          <Search size={20} />
          <Input 
            placeholder={t('doctorList.searchPlaceholder')} 
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </SearchBox>
        
        <CheckboxLabel>
          <input 
            type="checkbox" 
            checked={availableWithin2Weeks}
            onChange={(e) => setAvailableWithin2Weeks(e.target.checked)}
          />
          {t('doctorList.availableWithin')}
        </CheckboxLabel>

        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginLeft: 'auto' }}>
          <Filter size={20} color="#94a3b8" />
          <Select 
            style={{ width: '200px' }} 
            value={sortOption}
            onChange={(e) => setSortOption(e.target.value)}
          >
            <option value="rating">{t('doctorList.sortRating')}</option>
            <option value="name-asc">{t('doctorList.sortNameAsc')}</option>
            <option value="name-desc">{t('doctorList.sortNameDesc')}</option>
          </Select>
        </div>
      </ControlsContainer>

      {loading ? (
        <div style={{ padding: '2rem', textAlign: 'center', color: '#94a3b8' }}>{t('doctorList.loading')}</div>
      ) : (
        <Grid>
          {filteredDoctors.length === 0 ? (
            <div style={{ padding: '2rem', textAlign: 'center', color: '#94a3b8', gridColumn: '1 / -1' }}>
              {t('doctorList.noDoctorsFound')}
            </div>
          ) : (
            filteredDoctors.map(doc => {
              const reviewCount = doc.reviewsCount || 0;
              const avgRating = doc.averageRating 
                ? doc.averageRating.toFixed(1) 
                : t('doctorList.new');

              return (
                <DoctorCard key={doc.id}>
                  <RatingPill>
                    <Star size={16} fill="#eab308" color="#eab308" />
                    {avgRating}
                  </RatingPill>

                  <NameSection>
                    <h3>{t('patientDashboard.dr')} {doc.firstName} {doc.lastName}</h3>
                    <div className="specialty">{doc.specialization}</div>
                  </NameSection>

                  <InfoGrid>
                    <div><MessageSquare size={18} /> {reviewCount} patient reviews</div>
                  </InfoGrid>

                  <ActionRow>
                    <Button onClick={() => navigate(`/patient/book/${doc.id}`, { state: { doctor: doc } })} style={{ background: '#2563eb', color: 'white' }}>
                      Book Appointment
                    </Button>
                    <Button variant="secondary" onClick={() => handleDoctorClick(doc.id)} style={{ background: 'transparent', borderColor: '#cbd5e1' }}>
                      <Info size={16} style={{ marginRight: '0.5rem' }} /> View Full Profile
                    </Button>
                    <div style={{ marginLeft: 'auto' }}>
                      <AvailableBadge>Available</AvailableBadge>
                    </div>
                  </ActionRow>
                </DoctorCard>
              );
            })
          )}
        </Grid>
      )}
    </PageContainer>
  );
};

export default DoctorList;
